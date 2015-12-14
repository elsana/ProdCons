package jus.poc.prodcons.v4;

public class Semaphore {

	private int residu;

	public Semaphore(int init) {
		residu = init;
	}

	public synchronized void attendre() {
		while (this.residu == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.residu--;
	}

	public synchronized void reveiller(int nbE) { // Réveille nbE Consommateurs
													// quand une ressource est
													// placée
		this.residu = residu + nbE;
		notify();
	}

}
