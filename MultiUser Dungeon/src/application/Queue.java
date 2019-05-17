package application;


public class Queue<T> {
	class Node {
		Node next;
		T data;
		private Node(T data) { 
			this.data = data; 
		}
	}
	
	private Node head, tail;
	
	public Queue() { 
		head = tail = null; 
	}
	
	public void enqueue(T data) {
		if(head ==null) {
			head = new Node(data);
			tail = head;
		}
		else {
			tail.next = new Node(data);
			tail = tail.next;
		}
	}
	
	public T dequeue() {
		if(head==null)
			return null;
		T data = head.data;
		head = head.next;
		return data;
	}
	
	public boolean isEmpty() {
		return head==null;
	}
}
