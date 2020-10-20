package com.company;

import org.opencv.core.Core;
import org.opencv.highgui.*;
import org.opencv.core.CvType;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.*;

import java.time.temporal.TemporalAmount;

public class Main {



    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String[] args) {
        String filepath = "images/m.jpg"; //"images/colors.jpg"

        Mat colors= new Mat();
        Mat labels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS+TermCriteria.COUNT,10, 1.0);

        int clusters = 3;

        //Reading Image
        Mat img = Imgcodecs.imread(filepath);

        //Making image RGB
        Imgproc.cvtColor(img,img,4);

        //reshape into a single long line for Kmeans method
        img = img.reshape (3, img.rows() * img.cols());

        //Converting to np.float32 as required by Kmeans method
        img.convertTo(img,CvType.CV_32F);

        //Executing Kmeans
        Core.kmeans(img,clusters,labels, criteria,10,Core.KMEANS_RANDOM_CENTERS,colors);

        String dump = colors.dump();
        System.out.println(dump);

        //Showing Image
        //HighGui.imshow("img",img);
        //HighGui.waitKey(0);
    }
}
