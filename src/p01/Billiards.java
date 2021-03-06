package p01;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Billiards extends JFrame {

	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;

	// TODO update with number of group label. See practice statement.
	private final int N_BALL = 8;
	private Ball[] balls = new Ball[N_BALL]; //bolas
	private ThreadBall[] listaHilos = new ThreadBall[N_BALL];

	private volatile boolean running = false;

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
	}

	private void initBalls() {
		// TODO init balls
		for(int i = 0; i< N_BALL;i++){
			Ball ball = new Ball();
			balls[i]=ball;
		}
	}

	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when start button is pushed
			ThreadBall hilo;
			board.setBalls(balls);
			
			running = true;
			
			for (int i = 0; i<N_BALL; i++){
				hilo = new ThreadBall(balls[i]);
				listaHilos[i] = hilo;
				listaHilos[i].start();
			}
		}
	}

	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when stop button is pushed
			running = false;
			for(int i = 0; i<N_BALL;i++){
				listaHilos[i].interrupt();
			}

		}
	}

	private class ThreadBall extends Thread {
		
		private Ball ball;
		public ThreadBall(Ball pelota){
			this.ball=pelota;
		}
		
		public void run(){
			while(running){
				try{
					ball.move();
					board.repaint();
					Thread.sleep(6);
				}catch(InterruptedException e){
					System.err.println("Sleeping Thread Interrupted");
				}
			}
		}
	}

	public static void main(String[] args) {
		new Billiards();
	}
}