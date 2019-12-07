/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nearest.neighbor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Math.sqrt;
import java.util.Arrays;
import java.util.Scanner;
/**
 *
 * @author Shaf
 */
public class NearestNeighbor {
    
       int [][] dataset = new int [75][3];
       int [][] trainingData = new int[60][3];
       int [][] testingData = new int [16][3];
       int [] trainingLabel = new int[60];
       int [] testingLabel = new int[16];
       int [] knn = new int[15];
       String nama = "D:/PENS/SMT05/Mesin Learning/praktikum/Nearest Neighbor/src/nearest/neighbor/ruspini.csv";
       int k=0;

    public int[][] readData(String nama){
        BufferedReader br;
        String line;
        try {
            int i=0;
            br=new BufferedReader(new FileReader(nama));
            while((line=br.readLine())!=null){
                String[] ruspini=line.split(",");
                for(int j=0;j<3;j++){
                    dataset[i][j] = Integer.parseInt(ruspini[j]);
                }
                i++;   
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dataset;
    }
    
    public int dataClass(int data[][], int kelas){
        int jumlahData=0;
        for(int i=0;i<data.length;i++){
          for(int j=0;j<3;j++){
             if(j==2 && data[i][j]==kelas){
               jumlahData++;
             }
          }
        }    
        return (jumlahData);
    }
    
    public int[][] readTrainingData(int[][] dataSet, int splitRasio){
        int idxDataSet=0;
        int idxDataTrening=0;
        for(int i=1;i<=4;i++){
            int jumlahData=dataClass(dataSet,i);
            int jumlahDataTest=(jumlahData*80)/100;
            for(int j=idxDataTrening;j<(idxDataTrening+jumlahDataTest);j++){
               trainingData[j]=dataSet[idxDataSet];   
               idxDataSet++;
            }
               idxDataTrening=(idxDataTrening+jumlahDataTest);
               idxDataSet=idxDataSet+(jumlahData-jumlahDataTest);
        }
        return trainingData;
    }
    
     public int[][] readTestingData(int[][] dataSet, int splitRasio){
        int idxDataSet=0;
        int idxDataTesting=0;
        for(int i=1;i<=4;i++){
            int jumlahData=dataClass(dataSet,i);
            int jumlahDataTrening=(jumlahData*80)/100;
            int jumlahDataTesting=jumlahData-jumlahDataTrening;
            idxDataSet+=jumlahDataTrening;
            for(int j=idxDataTesting;j<(idxDataTesting+jumlahDataTesting);j++){
               testingData[j]=dataSet[idxDataSet];   
               idxDataSet++;
            }
               idxDataTesting=(idxDataTesting+jumlahDataTesting);
        }
        return testingData;
    }
     
    public int[] readTrainigLabel(int[][] trainingData){
       for(int i=0;i<trainingData.length;i++){
          for(int j=0;j<3;j++){
            if(j==2){
              trainingLabel[i]=trainingData[i][j];
            }
        }
       }
       return trainingLabel;
    }
    
    public int[] readTestingLabel(int[][] testingData){
       for(int i=0;i<testingData.length;i++){
          for(int j=0;j<3;j++){
            if(j==2){
              testingLabel[i]=testingData[i][j];
            }
        }
       }
       return testingLabel;
    }
    
    public double getDistance (int[] testingData_i,int[] trainingData_j){
       double distance = sqrt(((trainingData_j[0]-testingData_i[0])*
                                (trainingData_j[0]-testingData_i[0]))+
                                ((trainingData_j[1]-testingData_i[1])*
                                (trainingData_j[1]-testingData_i[1])));
       return distance;
    }
    
    public double[] allDistance (int []testingData_i,int trainingData[][]){
       double alldistances [] = new double[trainingData.length];
       for(int i=0;i<testingData_i.length;i++){
           for(int j=0;j<trainingData.length;j++){
               alldistances[j]=getDistance(testingData_i,trainingData[j]);
              System.out.println(alldistances[j]);
           }
       }
       return alldistances;
    }
    
    public double[][] newIndexData(double []alldistance){
       double[][] newIndexData = new double[alldistance.length][2];
        for(int i=0;i<alldistance.length;i++){
           for(int j=0;j<2;j++){
               if(j==0){
                 newIndexData[i][j]=alldistance[i];
               }else{
                newIndexData[i][j]=i;
               }
           }
        }
      return newIndexData;
    }
   
    public double[][] sortedData(double []alldistance) {
         double[][]sortedData = new double[alldistance.length][2];
         double[][] temp = new double[1][2];
         sortedData=newIndexData(alldistance);
         for(int i=1;i<sortedData.length;i++){
            temp[0] = sortedData[i];
            int j=i-1;
            while(j>=0 && temp[0][0]<sortedData[j][0]){
               sortedData[j+1]= sortedData[j];
               j--;
            }
            sortedData[j+1]=temp[0];
         }
        return sortedData;
   }
  
    public int classification_result (int k,double[][]sortedData, int[] tariningLabel){
        int knn=0;
        int indexData[]= new int[60];
        int tampung[]=new int[k];
        for(int i=0;i<sortedData.length;i++){
           for(int j=0;j<2;j++){
             if(j==1){
                   indexData[i]=tariningLabel[(int)sortedData[i][1]];   
             }
           }
        }
         for(int i=0;i<k;i++){
             tampung[i]=indexData[i];
         }
        knn= valueKnn(k,tampung);
        return knn;
    }
    
    
    public int valueKnn(int k,int[]temp){
        int knn=0;
        int tmtData[]={0,0,0,0,0};
        int besar;
        if(k==1){
            knn=temp[0];
        }else{
            for(int i=0;i<temp.length-1;i++){
                for(int j=1;j<=4;j++){
                   if(temp[i]==j){
                      tmtData[j]+=1;
                   }
                }
            }
            besar=tmtData[0];
            for(int i=0;i<tmtData.length;i++){
               if(tmtData[i]> besar){
                   besar=i;
               }
            }
            knn=besar;    
        }  
        return knn;
    } //salah di sini tolong di benerin 
    
    public int [] testingAllData(int k){
      int[] knn = new int [testingLabel.length];
      double[][] sortedData = new double[trainingData.length][2];
      double[] allDistance = new double[trainingData.length];
      for(int i=0;i<testingLabel.length;i++){
          allDistance = allDistance(testingData[i],trainingData);
          sortedData = sortedData(allDistance);
          knn[i]= classification_result(k,sortedData,trainingLabel);
      }
       return knn;
    }
    
    public double getError(int [] testingLabel,int[] knn){
       double error=0;
       int equals=0;
       for(int i=0;i<knn.length;i++){
         if(testingLabel[i]!=knn[i]){
            equals++;
         } 
       }
       error=(equals*100)/testingLabel.length;
       return (error);
    }
    
    public int inputK(){
       int k=0;
        Scanner input = new Scanner(System.in);
        System.out.print("masukan nilai k "+" = ");
       k = Integer.parseInt( input.nextLine());  
       return k;
    }
    
    public void runProgram(){
    
       k=inputK();
       dataset =readData(nama);
       testingData=readTestingData(dataset, 16);
       trainingData=readTrainingData(dataset, 60);
       testingLabel=readTestingLabel(testingData);
       trainingLabel=readTrainigLabel(trainingData);
       knn=testingAllData(k);
       double error=getError(testingLabel,knn);
       System.out.println("ERROR = "+error+"%");
    }
    
    
    
    public static void main(String[] args) {
       NearestNeighbor nn = new  NearestNeighbor();
       for(int i=0;i<3;i++){
         nn.runProgram();
       }
      } 
}
