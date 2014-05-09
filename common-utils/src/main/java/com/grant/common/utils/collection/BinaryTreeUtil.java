package com.grant.common.utils.collection;

import java.util.Random;

public class BinaryTreeUtil<E extends Comparable> {
	private BinaryTree<E> tree;
	private int size;
	
	public BinaryTreeUtil() {
	}
	
	public void add(E e){
		if(tree==null){
			tree = new BinaryTree<E>(e);
		}
		tree.add(e);
		size++;
	}
	
	public int getSize(){
		return size;
	}
	
	public static void main(String[] args) {
		
	BinaryTree<testEnt> tree  = new BinaryTree<testEnt>(new testEnt(0));
		Random ran = new Random();
		int max = 1000;
		int c = 10;
		tree.add(new testEnt(0));
		tree.add(new testEnt(0));
		tree.add(new testEnt(0));
		
		for (int i = 0; i < c; i++) {
//			int x = ran.nextInt(max);
//			System.out.println(x);
			tree.add(new testEnt(i));
		}
		
		tree.add(new testEnt(0));
		
		BinaryTree t = tree;
		int i = 0;
		while(t!=null){
			t = t.getRight();
			System.out.println(t+" depth:"+t.getDepth());
		}
	}
	
}

class testEnt implements Comparable{
	private int val;
	
	public testEnt(int val) {
		this.val = val;
	}
	
	@Override
	public int compareTo(Object o) {
		if(((testEnt)o).val<val){
			return 1;
		}else if(((testEnt)o).val==val){
			return 0;
		}else{return -1;}
	}
	
	@Override
	public String toString() {
		return super.toString()+" val:"+val;
	}
}
