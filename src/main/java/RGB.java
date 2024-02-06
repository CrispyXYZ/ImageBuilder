import static java.lang.Math.*;

public class RGB {
    public final int proto;
    public final int r;
    public final int g;
    public final int b;

    public RGB(int proto){
        this.proto=proto&0xffffff;
        this.r=(proto&0xff0000)>>16;
        this.g=(proto&0xff00)>>8;
        this.b=(proto&0xff);
    }

    public RGB(int r, int g, int b){
        this.r=r;
        this.g=g;
        this.b=b;
        this.proto=(r<<16)|(g<<8)|b;
    }

    public double getDifference(RGB that) {
        int[] thisLab = rgbToLab(r, g, b);
        int[] thatLab = rgbToLab(that.r, that.g, that.b);
        return pow(thisLab[0]-thatLab[0], 2) + pow(thisLab[1]-thatLab[1], 2) + pow(thisLab[2]-thatLab[2], 2);
    }

    private int[] rgbToLab(int r, int g, int b) {
        return new int[] {
                        (13933*r + 46871*g + 4732*b)  >> 16,
                ((377 * (14503*r - 22218*g + 7714*b)) >> 24) + 128,
                ((160 * (12773*r + 39695*g -52468*b)) >> 24) + 128
        };
    }

}