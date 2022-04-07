package Tetris;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {
    //游戏的行数26和列数12
    private static final int game_x = 26;//final关键字代表这个变量只被赋值一次
    private static final int game_y = 12;
    //文本域数组
    JTextArea[][] text;
    //二维矩阵，存储有没有方块
    int [][] data;

    public void initWindow(){
        //设置窗口大小
        this.setSize(600,900);
        //设置窗口是否可见
        this.setVisible(true);
        //设置窗口居中
        this.setLocationRelativeTo(null);//？
        //设置释放窗体
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口大小不可变
        this.setResizable(false);
        //设置标题
        this.setTitle("俄罗斯方块");
    }
    //初始化界面
    public void initGamePanel(){
        JPanel GamePanel=new JPanel();
        GamePanel.setLayout(new GridLayout(game_x,game_y,1,1));
    }

    public Tetris(){//构造函数
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        initWindow();
    }

    public static void main(String[] args) {
        Tetris tetris=new Tetris();
    }
}
