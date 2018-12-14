package com.example.zhouxf.collideball.bean;

/**
 * 二维向量
 * 当向量为零向量时，可能报错！！！
 */
public class VectorTwo {

    private float x;

    private float y;

    public VectorTwo(Point startPoint, Point endPoint){
        this.x=endPoint.getX()-startPoint.getX();
        this.y=endPoint.getY()-startPoint.getY();
    }

    public VectorTwo() {
        this.x = 0;
        this.y = 0;
    }

    public VectorTwo(VectorTwo v){
        this.x=v.getX();
        this.y=v.getY();
    }

    public VectorTwo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 得到相反向量
     * @return 返回-v，原值改变
     */
    public VectorTwo opposite(){
        this.x=-this.x;
        this.y=-this.y;
        return this;
    }
    /**
     * 得到相反向量
     * @return 返回-v，原值不变
     */
    public static VectorTwo opposite(VectorTwo v){
        return new VectorTwo(-v.getX(),-v.getY());
    }

    /**
     * 向量取模
     * @return 模长
     */
    public float magnitude(){
        return (float) Math.sqrt(x*x+y*y);
    }

    /**
     * 向量取模
     * @return 模长
     */
    public static float magnitude(VectorTwo v){
        return (float) Math.sqrt(v.x*v.x+v.y*v.y);
    }

    /**
     * 得到方向向量/单位向量
     * @return 自身变成单位向量
     */
    public VectorTwo unit(){
//        Log.e("VectorTwo","x="+x+",y="+y+",mo="+magnitude());
        float magnitude=magnitude();
        x/=magnitude;
        y/=magnitude;
        return this;
    }

    /**
     * 判断this是否在v1和v2两个向量中间
     * @param v1
     * @param v2
     * @return
     */
    public boolean isBetween(VectorTwo v1,VectorTwo v2){
        return getRadian(this,v1)+getRadian(this,v2)<getRadian(v1,v2)+0.0001;
    }

    public static boolean isBetween(VectorTwo v,VectorTwo v1,VectorTwo v2){
        return getRadian(v,v1)+getRadian(v,v2)<getRadian(v1,v2)+0.00001;
    }

    /**
     *  得到方向向量/单位向量
     * @param v
     * @return 单位向量
     */
    public static VectorTwo unit(VectorTwo v){
        return new VectorTwo(v.x/magnitude(v),v.y/magnitude(v));
    }

    /**
     * 向量的加法
     * @param v 被加的向量
     * @return 向量实例+=v
     */
    public VectorTwo add(VectorTwo v){
        this.x+=v.getX();
        this.y+=v.getY();
        return this;
    }

    /**
     * 向量的加法
     * @param v1 向量1
     * @param v2 向量2
     * @return v1+v2
     */
    public static VectorTwo add(VectorTwo v1, VectorTwo v2) {
        return new VectorTwo(v1.getX()+v2.getX(),v1.getY()+v2.getY());
    }

    /**
     *  向量减法
     * @param v 要减去的向量
     * @return 向量实例 -= v
     */
    public VectorTwo sub(VectorTwo v){
        this.x-=v.getX();
        this.y-=v.getY();
        return this;
    }

    /**
     * 向量减法
     * @param v1 被减向量
     * @param v2 要减去的向量
     * @return v1-v2
     */
    public static VectorTwo sub(VectorTwo v1, VectorTwo v2){
        return add(v1,opposite(v2));
    }


    /**
     *  向量的数乘
     * @param factor 乘数
     * @return 结果返回至调用该方法的对象
     */
    public VectorTwo scalarProduct(float factor){
        x*=factor;
        y*=factor;
        return this;
    }

    /**
     *  向量数乘
     * @param factor 乘数
     * @param v 向量
     * @return 静态方法
     */
    public static VectorTwo scalarProduct(float factor, VectorTwo v){
        return new VectorTwo(factor*v.getX(),factor*v.getY());
    }

    /**
     *  向量点乘
     * @param v
     * @return float
     */
    public float dotProduct(VectorTwo v){
        return this.x*v.x+this.y*v.getY();
    }

    /**
     *  向量点乘
     * @param v1
     * @param v2
     * @return float
     */
    public static float dotProduct(VectorTwo v1, VectorTwo v2){
        return v1.x*v2.x+v1.y*v2.y;
    }

    /**
     *  得到两个向量夹角余弦值
     * @param v1
     * @param v2
     * @return
     */
    public static float getCosA(VectorTwo v1, VectorTwo v2){
        return dotProduct(v1,v2)/(v1.magnitude()*v2.magnitude());
    }

    /**
     * 得到v1在v2上的投影(取值可能是负数的长度，不是向量)
     */
    public static float getProjection(VectorTwo v1, VectorTwo v2){
        return dotProduct(v1,v2)/v2.magnitude();
    }

    /**
     * 得到v1在v2上的投影向量(投影向量与v2方向相同或相反，与v1夹角小于90)
     * @return VectorTwo
     */
    public static VectorTwo getProjectionVector(VectorTwo v1, VectorTwo v2){
        return unit(v2).scalarProduct(getProjection(v1, v2));
    }

    /**
     *  得到两个向量夹角弧度
     * @param v1
     * @param v2
     * @return
     */
    public static float getRadian(VectorTwo v1, VectorTwo v2){
        return (float) Math.acos(getCosA(v1,v2));
    }

    /**
     *  得到两个向量夹角角度
     * @param v1
     * @param v2
     * @return
     */
    public static float getAngle(VectorTwo v1, VectorTwo v2){
        return (float) (getRadian(v1,v2)/Math.PI*180);
    }

    /**
     * 判断两个向量方向是否相同
     * @param v1
     * @param v2
     * @return
     */
    public static Boolean sameDirection(VectorTwo v1, VectorTwo v2){
        return getCosA(v1, v2) == 1;
    }

    /**
     * 得到一个向量的垂直向量
     * @param v
     * @return
     */
    public static VectorTwo getPerpendicular(VectorTwo v){
        return new VectorTwo(v.getY(),-v.getX());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y +")" ;
    }
}
