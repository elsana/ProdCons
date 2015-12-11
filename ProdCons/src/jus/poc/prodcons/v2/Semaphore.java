package jus.poc.prodcons.v2;

public class Semaphore {
	public void attendre() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void reveiller() {
		notify();
	}
}
