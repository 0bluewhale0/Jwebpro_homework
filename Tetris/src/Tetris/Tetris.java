package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Tetris extends JFrame implements KeyListener {
    //游戏的行数26和列数12
    private static final int game_x = 26;//final关键字代表这个变量只被赋值一次
    private static final int game_y = 12;
    //文本域数组
    JTextArea[][] text;
    //二维矩阵，存储有没有方块
    int [][] data;
    //显示游戏状态的标签
    JLabel label1;
    //显示游戏分数的标签
    JLabel label;
    public void initWindow(){
        //设置窗口大小
        this.setSize(600,850);
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
        JPanel game_main=new JPanel();
        game_main.setLayout(new GridLayout(game_x,game_y,1,1));
        //初始化面板
        for(int i=0;i<text.length;i++)
        {
            for(int j=0;j<text[i].length;j++)
            {
                text[i][j] = new JTextArea(game_x,game_y);
                text[i][j].setBackground(Color.pink);
                text[i][j].addKeyListener(this);
                //初始化游戏边框
                if(i==0||i==text.length-1||j==0||j==text[i].length-1)//i=0有什么影响？
                {
                    text[i][j].setBackground(Color.orange);
                    data[i][j]=1;
                }
                text[i][j].setEditable(false);
                game_main.add(text[i][j]);
            }
        }
        this.setLayout(new BorderLayout());
        this.add(game_main,BorderLayout.CENTER);
    }

    //初始化游戏的说明面板
    public void initExplainPanel() {
        //添加左右控制面板并且初始化
        JPanel explain_left=new JPanel();
        JPanel explain_right=new JPanel();
        explain_left.setLayout(new GridLayout(4,1));
        explain_right.setLayout(new GridLayout(2,1));
        //添加说明文字
        explain_left.add(new JLabel("按空格键，方块变形"));
        explain_left.add(new JLabel("按左方向键，方块左移"));
        explain_left.add(new JLabel("按右方向键，方块右移"));
        explain_left.add(new JLabel("按下方向键，方块下落"));
        //设置标签内容颜色
        label1.setForeground(Color.red);
        //添加游戏状态、游戏分数到右说明面板
        explain_right.add(label);
        explain_right.add(label1);
        this.add(explain_left,BorderLayout.WEST);
        this.add(explain_right,BorderLayout.EAST);
    }

    public Tetris(){//构造函数
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        //初始化状态和分数标签
        label1=new JLabel("游戏状态：游戏中...");
        label = new JLabel("游戏得分：0 分");
        initGamePanel();
        initExplainPanel();
        initWindow();
    }

    public static void main(String[] args) {
        Tetris tetris=new Tetris();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
