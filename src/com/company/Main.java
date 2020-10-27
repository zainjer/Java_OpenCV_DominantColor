package com.company;

import org.opencv.core.Core;
import org.opencv.highgui.*;
import org.opencv.core.CvType;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.List;

public class Main {

    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String[] args) throws IOException {

        String filepath = "images/face1.jpg";
        //String filepath = "images/face2.jpg";

        int clusters = 5;

        Mat colors= new Mat();
        Mat labels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS+TermCriteria.COUNT,10, 1.0);

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

        int row = 0;
        int col = 0;
        int element = 0;

        float r = 0,g = 0,b = 0;

        char[] charList = new char[100];

        Color[] colorsArray = new Color[clusters];

        for(int i = 0; i<dump.length(); i++){
            if(dump.charAt(i)=='['){
                continue;
            }

            if(dump.charAt(i) == ',')
            {
                String s = new String(charList).trim();
                System.out.println(s);
                if(element==0) {
                    r = Math.round(Float.parseFloat(s));
                }else if(element == 1){
                    g = Math.round(Float.parseFloat(s));
                }
                else if(element == 2){
                    b = Float.parseFloat(s);
                }
                charList = new char[100];
                col = 0;
                element++;
                continue;
            }
            if(dump.charAt(i) == ';' || dump.charAt(i) == ']' ){
                if(element == 2){
                    String s = new String(charList).trim();
                    System.out.println(s);
                    b = Math.round(Float.parseFloat(s));
                }
                System.out.println(r+" "+g+" "+b+"");



                colorsArray[row] = new Color((int)r,(int)g,(int)b,255);

                row++;
                element = 0;
                charList = new char[100];
                col = 0;
                continue;
            }
//            if(dump.charAt(i) == '\n')
//                continue;

            charList[col]=dump.charAt(i);

            col++;
        }


        JFrame f = new JFrame();


        JPanel[] panels = new JPanel[clusters];

        for (int i = 0; i<clusters; i++)
        {
            panels[i] = new JPanel();
            panels[i].setBounds(0,100*i,100,100);
            panels[i].setBackground(colorsArray[i]);
            panels[i].setVisible(true);
            f.add(panels[i]);
        }

        BufferedImage myPicture = ImageIO.read(new File(filepath));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setBounds(100,0,myPicture.getWidth(),myPicture.getHeight());

        int height = Math.max(myPicture.getHeight(),clusters*100);

        f.setSize(myPicture.getWidth()+100,height);
        f.setTitle("Color Tones");
        f.add(picLabel);
        f.setLayout(null);
        f.setVisible(true);
    }
}
