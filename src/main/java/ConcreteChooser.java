public class ConcreteChooser {
    public static final RGB RED = new RGB(11546150);
    public static final RGB GREEN = new RGB(6192150);
    public static final RGB PURPLE = new RGB(8991416);
    public static final RGB CYAN = new RGB(1481884);
    public static final RGB SILVER = new RGB(10329495);
    public static final RGB GRAY = new RGB(4673362);
    public static final RGB PINK = new RGB(15961002);
    public static final RGB LIME = new RGB(8439583);
    public static final RGB YELLOW = new RGB(16701501);
    public static final RGB LIGHT_BLUE = new RGB(3847130);
    public static final RGB MAGENTA = new RGB(13061821);
    public static final RGB ORANGE = new RGB(16351261);
    public static final RGB BLACK = new RGB(1908001);
    public static final RGB BROWN = new RGB(8606770);
    public static final RGB BLUE = new RGB(3949738);
    public static final RGB WHITE = new RGB(16383998);

    public static String choose(RGB rgb){
        double diff=Double.MAX_VALUE;
        RGB choice=new RGB(0);
        for(RGB concrete: new RGB[]{RED,GREEN,PURPLE,CYAN,SILVER,GRAY,PINK,LIME,YELLOW,LIGHT_BLUE,MAGENTA,ORANGE,BLACK,BROWN,BLUE,WHITE}) {
            double diff2=rgb.getDifferent(concrete);
            if(diff2<diff){
                diff=diff2;
                choice=concrete;
            }
        }
        int dv;
        switch(choice.proto){
            case 11546150:
                dv=14;
                break;
            case 6192150:
                dv=13;
                break;
            case 8991416:
                dv=10;
                break;
            case 1481884:
                dv=9;
                break;
            case 10329495:
                dv=8;
                break;
            case 4673362:
                dv=7;
                break;
            case 15961002:
                dv=6;
                break;
            case 8439583:
                dv=5;
                break;
            case 16701501:
                dv=4;
                break;
            case 3847130:
                dv=3;
                break;
            case 13061821:
                dv=2;
                break;
            case 16351261:
                dv=1;
                break;
            case 1908001:
                dv=15;
                break;
            case 8606770:
                dv=12;
                break;
            case 3949738:
                dv=11;
                break;
            case 16383998:
            default:
                dv=0;
        }
        return "concrete "+dv;
    }
}