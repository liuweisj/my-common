package com.grant.common.utils.collection;

public class BinaryTree<E extends Comparable>{
		
		public BinaryTree(E e) {
			this.value = e;
			this.depth = 0;
			this.max = this;
			this.min = this;
		}
		
		private BinaryTree left;
		private BinaryTree right;
		
		private BinaryTree max;
		private BinaryTree min;
		
		private BinaryTree parent;
		private int depth;
		
		private E value;
		
		public int getDepth() {
			return depth;
		}

		public void add(E e){
			add(new BinaryTree<E>(e));
		}
		
		private void add(BinaryTree<E> entity){
			
			E e = entity.value;
			
			if(value.compareTo(e)>=0){
				if(this.left==null){
					this.left = entity;
					entity.parent = this;
					entity.depth = this.depth+1;
					this.min = entity;
					System.out.println(e+" add to:"+this.getValue()+" left.");
				}else{
					this.left.add(entity);
				}
			}else{
				if(this.right==null){
					this.right = entity;
					entity.parent = this;
					entity.depth = this.depth+1;
					this.max = entity;
					System.out.println(e+" add to:"+this.getValue()+" right.");
				}else{
					this.right.add(entity);
				}
			}
		}
		
		public E getValue() {
			return value;
		}
		public void setValue(E value) {
			this.value = value;
		}
		public BinaryTree getLeft() {
			return left;
		}
		
		public BinaryTree getRight() {
			return right;
		}
		
		public BinaryTree getParent() {
			return parent;
		}
		
		
		public BinaryTree getMax() {
			return max;
		}

		public BinaryTree getMin() {
			return min;
		}

		@Override
		public String toString() {
			
			return value.toString();
		}
	}