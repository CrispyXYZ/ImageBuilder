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

    public double getDifferent(RGB that){
        return sqrt(pow(this.r-that.r,2)+pow(this.g-that.g,2)+pow(this.b-that.b,2));
    }
}