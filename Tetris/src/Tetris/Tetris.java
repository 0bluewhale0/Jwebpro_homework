package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Tetris extends JFrame implements KeyListener {
    //游戏的行数26和列数12
    private static final int game_x = 26;//final关键字代表这个变量只被赋值一次
    private static final int game_y = 12;
    //文本域数组
    JTextArea[][] text;
    //二维矩阵，存储有没有方块
    int [][] data;
    //存放所有方块的数组：
    int [] allRect;
    //存放当前方块：
    int rect;
    //显示游戏状态的标签
    JLabel label1;
    //显示游戏分数的标签
    JLabel label;
    boolean isRunning;
    //方块坐标
    int x=0,y=0;
    //线程的休眠时间
    int time=1000;
    //得分
    int score=0;
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
        isRunning=true;//放的位置有影响吗？？
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        //初始化状态和分数标签
        label1=new JLabel("游戏状态：游戏中...");
        label = new JLabel("游戏得分：0 分");
        initGamePanel();
        initExplainPanel();
        initWindow();
        //初始化方块数组
        allRect=new int[]{0x00cc,0x8888,0x000f,0x888f,0xf888,0xf111,0x111f,0x0eee,0xffff
                ,0x0008,0x0888,0x000e,0x0088,0x000c,0x08c8,0x00e4,0x04c4,0x004e,0x08c4, 0x006c,0x04c8,0x00c6};
    }

    public static void main(String[] args) {

        Tetris tetris=new Tetris();
        tetris.game_begin();
    }

    public void game_begin()
    {
        while(true)
        {
            if(!isRunning)//不跑了
            {
                break;
            }
            //让游戏继续进行
            game_run();
        }
        label1.setText("游戏状态：游戏结束");//更改游戏状态
    }
    //随机生成下落方块的方法
    public void randomRect(){
        Random random=new Random();
        rect=allRect[random.nextInt(22)];//生成一个[0-22)的随机数
    }
    //游戏运行函数
    public void game_run()
    {
        randomRect();
        x=0;
        y=5;
        for(int i=0;i<game_x;i++)
        {
            try{
                Thread.sleep(time);
                //如果方块还可以下落
                if(canFall(x,y))
                {
                    //层数++
                    x++;
                    fall(x,y);
                }
                else
                {
                    //将data置为1
                    changeData(x,y);
                    //循环遍历4层，看是否可以消除行
                    for(int j=x;j<x+4;j++)
                    {
                        int sum=0;//统计有多少列有方块
                        for(int k=1;k<game_y-1;k++)
                        {
                            if(data[j][k]==1)
                                sum++;
                        }
                        //判断是否有一行可以消除
                        if(sum==game_y-2)
                        {
                            //消除这一行
                            removeRow(j);
                        }
                    }
                    //判断是否失败
                    for(int j=1;j<game_y-1;j++)
                    {
                        if(data[3][j]==1)
                        {
                            isRunning=false;
                            break;
                        }
                    }
                    break;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
//判断是否可以下降
    public boolean canFall(int m,int n) {
        int temp=0x8000;
        //遍历整个方格
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if((temp & rect)!=0)//有方块
                {
                    //判断下一行是否有方块
                    if(data[m+1][n]!=0)
                        return false;
                }
                n++;
                temp>>=1;
            }
            m++;
            n=n-4;
        }
        //可以下落
        return true;
    }
//改变不可下降的方块对应的区域值的方法
    public void changeData(int m,int n)
    {
        int temp=0x8000;
        //遍历整个方块
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if((temp&rect)!=0)
                {
                    data[m][n]=1;
                }
                n++;
                temp>>=1;
            }
            m++;
            n=n-4;
        }
    }

//移除某一行所有的方块，并且让上面的方块都掉落
    public void removeRow(int row)
    {
        int temp=100;
        for(int i=row;i>=1;i--)
        {
            for(int j=1;i<=game_y-2;j++)
            {
                //覆盖
                data[i][j]=data[i-1][j];
            }
        }
        //刷新游戏区域
        reflesh(row);

        //方块加速
        if(time>temp)
        {
            time-=temp;
        }
        score+=temp;
        //显示变化后的分数
        label.setText("游戏得分"+score);
    }

    //刷新移除某一行之后的游戏界面的方法
    public void reflesh(int row)
    {
        for(int i=row;i>=1;i--)
        {
            for(int j=0;j<=game_y-2;j++)
            {
                if(data[i][j]==1)//是否是方块
                {
                    text[i][j].setBackground(Color.white);
                }
                else
                {
                    text[i][j].setBackground(Color.pink);
                }
            }
        }
    }
//方块向下掉一层
    public void fall(int m,int n)
    {
        if(m>0)
        {
            //清除上一层的方块
            clear(m-1,n);
        }
        //重新绘制
        draw(m,n);
    }

// 清除上一层掉落后，有颜色的方块的方法
    public void clear(int m,int n)
    {
        int temp=0x8000;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if((temp&rect)!=0&&m!=0)
                {
                    text[m][n].setBackground(Color.pink);
                }
                n++;
                temp>>=1;
            }
            m++;
            n-=4;
        }
    }

   //重新绘制掉落后的方块
    public void draw(int m,int n)
    {
        int temp=0x8000;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if((temp&rect)!=0)
                {
                    text[m][n].setBackground(Color.WHITE);
                }
                n++;
                temp>>=1;
            }
            m++;
            n-=4;
        }
    }
//

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    //控制方块左移
        if(e.getKeyCode()==37)//左移箭头
        {
            if(isRunning==false)
            {
                return;
            }
            //判断是否碰墙了
            if(y<=1)
            {
                return;
            }
            //判断左边是否碰到了别的方块
            int temp=0x8000;
            for(int i=x;i<x+4;i++)
            {
                for(int j=y;j<y+4;j++)
                {
                    if((temp&rect)!=0)
                    {
                        if(data[i][j-1]==1)
                            return;
                    }
                    temp>>=1;
                }
            }
            //这时方块可以左移了
            clear(x,y);
            draw(x,--y);
        }
    //控制方块右移
        if(e.getKeyCode()==39)
        {
            if(isRunning==false)
            {
                return;
            }
            //判断是否碰到右墙壁
            //首先获取最右边的边界
            int temp=0x8000;
            int m=x;int n=y;
            int num=1;
            for(int i=0;i<4;i++)
            {
                for(int j=0;j<4;j++)
                {
                    if((temp&rect)!=0)
                    {
                        if(n>num)
                        {
                            num=n;
                        }
                    }
                    n++;
                    temp>>=1;
                }
                m++;n-=4;
            }
            if(num>=game_y-2)
            {
                return;
            }
            //判断右边是否碰到了别的方块
            temp=0x8000;
            for(int i=x;i<x+4;i++)
            {
                for(int j=y;j<y+4;j++)
                {
                    if((temp&rect)!=0)
                    {
                        if(data[i][j+1]==1)
                            return;
                    }
                    temp>>=1;
                }
            }
            //这时方块可以左移了
            clear(x,y);
            draw(x,++y);
        }
    //控制方块下落
        if(e.getKeyCode()==40)//方向键 下
        {
            if(isRunning==false)
            {
                return;
            }
            //判断还能接着下落吗
            if(!canFall(x,y))
            {
                return;
            }
            clear(x,y);
            draw(++x,y);
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
