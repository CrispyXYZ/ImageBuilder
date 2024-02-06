import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("SpellCheckingInspection")
public class MyWSS extends WebSocketServer {

    private final int port;
    private final int[] orig = new int[3];
    private long delay = 2L;
    private int rotate = 0;
    private boolean x = true, y = false, z = true;
    private static final Logger log = LoggerFactory.getLogger(MyWSS.class);

    public MyWSS(int port) {
        super(new InetSocketAddress(port));
        this.port = port;

        System.out.printf("Logger enabled: I:%s, W:%s, E:%s, D:%s\n", log.isInfoEnabled(), log.isWarnEnabled(), log.isErrorEnabled(), log.isDebugEnabled());
    }

    private void execute(WebSocket ws, String cmd) {
        ws.send("{\"body\":{\"origin\":{\"type\":\"player\"},\"commandLine\":\"" + cmd +
                "\",\"version\":1},\"header\":{\"requestId\":\"0ffae098-00ff-ffff-abbbbbbbbbdf3f44\",\"messagePurpose\":\"commandRequest\",\"version\":1,\"messageType\":\"commandRequest\"}}");
        log.info("命令已执行: /" + cmd);
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake shake) {
        log.info("客户端已连接！!");
        ws.send("{\"body\": {\"eventName\": \"PlayerMessage\"},\"header\": {\"requestId\": \"0ffae098-00ff-ffff-abbbbbbbbbdf3344\",\"messagePurpose\": \"subscribe\",\"version\": 1,\"messageType\": \"commandRequest\"}}");
        execute(ws, "say 当前版本:v1.0.0");
        execute(ws, "say \n使用set pos X Y Z以设置原点\n使用set delay NUMBER以设置每条命令执行间隔(单位:ms,默认为2)\n使用rotate以旋转\n使用switch以切换平面\n使用draw FILE以绘图(1像素=1格)");
    }

    @Override
    public void onClose(WebSocket ws, int p2, String p3, boolean p4) {
        log.info("客户端已掉线！");
    }

    @Override
    public void onMessage(WebSocket ws, String msg) {
        try {
            JSONObject json = new JSONObject(msg);
            if ("PlayerMessage".equals(json.getJSONObject("header").getString("eventName")) && "chat".equals(json.getJSONObject("body").getString("type"))) {
                String message = json.getJSONObject("body").getString("message");
                log.info("收到玩家消息:" + message);
                if (message.startsWith("set pos ")) {
                    String pos = message.substring(8);
                    String[] parsed = pos.split(" ");
                    orig[0] = Integer.parseInt(parsed[0]);
                    orig[1] = Integer.parseInt(parsed[1]);
                    orig[2] = Integer.parseInt(parsed[2]);
                    execute(ws, "say 已将原点设为" + Arrays.toString(orig));
                }
                if (message.startsWith("set delay ")) {
                    delay = Long.parseLong(message.substring(10));
                    execute(ws, "say 已将延迟设为" + delay);
                }
                if ("rotate".equals(message)){
                    rotate= ++rotate % 4;
                    execute(ws, "say 已将旋转角度设为"+rotate*90+"°");
                }
                if("switch".equals(message)){
                    x ^= y;
                    y ^= x;
                    x ^= y;
                    y ^= z;
                    z ^= y;
                    y ^= z;
                    execute(ws, "say 已切换为平面"+(x?"x":"")+(y?"y":"")+(z?"z":""));
                }
                if (message.startsWith("draw ")) {
                    String filename = message.substring(5);
                    new Thread(() -> {
                        try {
                            HashMap<Point2d, RGB> map = ImageParser.parseImage(filename);
                            for (Entry<Point2d, RGB> entry : map.entrySet()) {
                                //execute(ws,"title @a actionbar 已完成"+curr+"像素");
                                Point2d point = entry.getKey();
                                RGB color = entry.getValue();
                                String concrete = ConcreteChooser.choose(color);
                                String cmd;
                                if(x&&z) cmd = "setblock " +
                                        (orig[0] + (0<rotate&&rotate<3 ? -1 : 1)*(rotate%2==0 ? point.x : point.y)) + " " +
                                        orig[1] + " " +
                                        (orig[2] + (1<rotate ? -1 : 1)*(rotate%2==0 ? point.y : point.x)) + " " +
                                        concrete;
                                else if(x&&y) cmd = "setblock " +
                                        (orig[0] + (0<rotate&&rotate<3 ? -1 : 1)*(rotate%2==0 ? point.x : point.y)) + " " +
                                        (orig[1] + (1<rotate ? -1 : 1)*(rotate%2==0 ? point.y : point.x)) + " " +
                                        orig[2] + " " +
                                        concrete;
                                else if(y&&z) cmd = "setblock " +
                                        orig[0] + " " +
                                        (orig[1] + (1<rotate ? -1 : 1)*(rotate%2==0 ? point.y : point.x)) + " " +
                                        (orig[2] + (0<rotate&&rotate<3 ? -1 : 1)*(rotate%2==0 ? point.x : point.y)) + " " +
                                        concrete;
                                else cmd = "say 内部错误！";
                                execute(ws, cmd);
                                Thread.sleep(delay);
                            }
                        } catch (IOException e) {
                            execute(ws, "say " + e);
                        } catch (InterruptedException e) {
                            log.error(String.valueOf(e));
                        }
                    }).start();
                }
            }
        } catch (JSONException e) {
            log.warn(String.valueOf(e));
        }
    }

    @Override
    public void onError(WebSocket ws, Exception e) {
        log.error(String.valueOf(e));
    }

    @Override
    public void onStart() {
        try {
            log.info("已在"+ InetAddress.getLocalHost().getHostAddress() +":" + port + "建立WebSocket服务器！正在等待客户端......");
        } catch (UnknownHostException e) {
            log.warn(String.valueOf(e));
        }
    }
}
