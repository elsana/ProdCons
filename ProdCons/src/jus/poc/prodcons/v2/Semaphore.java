package jus.poc.prodcons.v2;

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

	public synchronized void reveiller() {
		this.residu++;
		notify();
	}

}
