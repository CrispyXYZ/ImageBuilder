import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageParser {
    public static HashMap<Point2d,RGB> parseImage(String filename) throws IOException{
        HashMap<Point2d,RGB> map = new HashMap<>();
        File file=new File(filename);
        BufferedImage bi = ImageIO.read(file);
        int width=bi.getWidth();
        int height=bi.getHeight();
        int minx=bi.getMinX();
        int miny=bi.getMinY();
        for(int i=minx;i<width;i++){
            for(int j=miny;j<height;j++){
                int pixel=bi.getRGB(i, j);
                RGB rgb = new RGB(pixel);
                Point2d point = new Point2d(i,j);
                map.put(point,rgb);
            }
        }
        return map;
    }
}