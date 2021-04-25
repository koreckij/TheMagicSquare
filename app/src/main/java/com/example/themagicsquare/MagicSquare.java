package com.example.themagicsquare;


import androidx.annotation.NonNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MagicSquare  implements Cloneable {
    private int[][] square;

    public MagicSquare(){
        square = new int[3][3];
        List<Integer> numbers= java.util.stream.IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
        for (int i=0;i<3;i++)
            for (int j=0;j<3;j++)
                square[i][j]= (int)numbers.get(i*3+j);
    }

    public int[] getRowSums(){
        int[] row=new int[3];
        for (int i=0;i<3;i++){
            int sum= 0;
            for (int j=0;j<3;j++){
                sum+=square[i][j];
            }
            row[i]=sum;
        }
        return row;
    }

    public int[] getColumnSums(){
        int[] column=new int[3];
        for (int i=0;i<3;i++){
            int sum= 0;
            for (int j=0;j<3;j++){
                sum+=square[j][i];
            }
            column[i]=sum;
        }
        return column;
    }

    public int getHelp(int row,int column){
        return square[row][column];
    }

    public boolean checkSulution(int[][] solution){
        int[] rowSums=getRowSums();
        int[] columnSums=getColumnSums();
        for (int i=0;i<3;i++){
            int solutionRow = 0;
            int solutionColumn = 0;
            for (int j = 0;j < 3;j++){
                solutionRow+=solution[i][j];
                solutionColumn += solution[j][i];
            }
            if(solutionRow!=rowSums[i] || solutionColumn!=columnSums[i])
                return false;
        }
        return true;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        MagicSquare magicSquare = (MagicSquare) super.clone();
        magicSquare.square=square.clone();
        return  magicSquare;
    }
}
