//========================================
// Imports
//========================================

class DLList<T> {
	
	//========================================
	// (Internal) DLListNode Class
	//========================================
	private class DLListNode<T>{
		// data members
		public T data;
		public DLListNode<T> previous;
		public DLListNode<T> next;
		
		// overloaded constructor
		DLListNode(T value){
			data = value;
			previous = null;
			next = null;
		}
		
	}
	
	//========================================
	// Data Members
	//========================================
	private DLListNode<T> front;
	private DLListNode<T> back;
	private DLListNode<T> current;
	int size;
	int index;
	
	//========================================
	// Member Functions (Methods)
	//========================================
	
	// default constructor
	public DLList() {
		clear();
	}
	
	// copy constructor (deep copy)
	public DLList(DLList<T> other) {
		
	}
	
	// clear list method
	public void clear() {
		front = null;
		back = null;
		current = null;
		size = 0;
		index = -1;
	}
	
	// get size method
	public int getSize() {
		return size;
	}
	
	// get index method
	public int getIndex() {
		return index;
	}
	
	// is empty method
	public boolean isEmpty() {
		return (getSize() == 0);
	}
	
	// is at first node method
	public boolean atFirst() {
		return (getIndex() == 0);
	}
	
	// is at last node method
	public boolean atLast() {
		return (getIndex() == (getSize() - 1));
	}
	
	// get data at current method
	public T getData(){
		if (!isEmpty()) {
			return current.data;
		} else {
			return null;
		}
	}
	
	// set data at current method
	public T setData(T x){
		if (!isEmpty()) {
			current.data = x;
			return x;
		} else {
			return null;
		}
	}
	
	// seek to first node method
	public boolean first() {
		return (seek(0));
	}
	
	// seek to the next node method (move from current node to next node)
	public boolean next() {
		return (seek(getIndex() + 1));	
	}
	
	// seek to previous node method
	public boolean previous() {
		return (seek(getIndex() - 1));		
	}
	
	// seek to last node method
	public boolean last() {
		return (seek(getSize() - 1));	
	}
	
	// seek method
	public boolean seek(int loc) {
		// local variables
		boolean retval = false;
		
		// is the list empty
		if (isEmpty()) {
			retval = false;
		// is loc in range
		} else if(loc < 0 || loc >= getSize()) {
			return false;
		// is loc == 0
		} else if (loc == 0) {
			current = front;
			index = 0;
			retval = true;
		// is loc == last index
		} else if (loc == getIndex()) {
			current = back;
			index = getSize() - 1;
			retval = true;
		// is loc < current index
        } else if (loc < getIndex()) {
			for(; getIndex() != loc; index--) {
				current = current.previous;
			}
			retval = true;
        // is loc > current index
		} else if (loc > getIndex()){
            for(; getIndex() != loc; index++){
                current = current.next;
            }
            retval = true;
        // else ... loc is at the current index ... nothing to do
        } else {
            retval = true;
        }
		
        return retval;
	}
	
	// insert first method
	public boolean insertFirst(T item) {
        // local variables
        DLListNode<T> newnode;
        boolean retval = false;

		newnode = new DLListNode<T>(item);

		if(isEmpty()){
			current = newnode;
			front = newnode;
			back = newnode;
			size++;
            index++;
			retval = true;
		} else {
			seek(0);
            newnode.next = front;
            newnode.previous = null;
            if(front != null){
                front.previous = newnode;
            }
            front = newnode;
            current = newnode;
            size++;
            retval = true;
		}

		return retval;
	}
	
	// insert at current location method
	public boolean insertAt(T item) {
		// local variables
        DLListNode<T> newnode;
        boolean retval = false;

		newnode = new DLListNode<T>(item);

		// list is empty or inserting first
		if(isEmpty() || getIndex() == 0){
			insertFirst(item);
			retval = true;
		} else {
			newnode.previous = current.previous;
			newnode.next = current;
			current.previous.next = newnode;
			current.previous = newnode;
			current = newnode;
			size++;
			retval = true;
		}

		return retval;
	}
	
	// insert last method
	public boolean insertLast(T item) {
		// local variables
        DLListNode<T> newnode;
        boolean retval = false;

		newnode = new DLListNode<T>(item);

		if(isEmpty()){
			insertFirst(item);
			retval = true;
		} else {
			last();
            newnode.previous = back;
            back.next = newnode;
            back = newnode;
            current = newnode;
            size++;
            index++;
            retval = true;
		}
        return retval;
	}
	
	// delete first method
	public boolean deleteFirst() {
		// local variables
		boolean retval = false;
		
		seek(0);
		
		if(isEmpty()){
			retval = false;
		} else if(getSize() == 1){
			clear();
			retval = true;
		} else if(current == front){
			current = current.next;
			front.next.previous = null;
			front = front.next;
			index--;
			size--;
			retval = true;
		}

		return retval;
	}
	
	// delete at current location method
	public boolean deleteAt() {
		// local variables
		boolean retval = false;

		if(isEmpty()){
			retval = false;
		} else if((getSize() == 1)){
			clear();
			retval = true;
		} else if(getSize() > 1){
			if(current == front){
				retval = deleteFirst();
			} else if(current == back){
				retval = deleteLast();
			} else {
				current.previous.next = current.next;
				current.next.previous = current.previous;
                DLListNode<T> delref = current;
				delref = current;
				current = current.next;
				delref.previous = null;
				delref.next = null;
				delref = null;
				size--;
				retval = true;
			}
		}

		return retval;
	}
		
	// delete last method
	public boolean deleteLast() {
		// local variables
		boolean retval = false;

		seek(getSize() - 1);
		
		if(isEmpty()){
			retval = false;
		} else if(getSize() == 1){
			clear();
			retval = true;
		} else if(current == back){
			current = current.previous;
			back.previous.next = null;
			back = back.previous;
			index--;
			size--;
			retval = true;
		}

		return retval;
	}

    // returns true if there is a next node
    public boolean hasNext(){
        if(current.next == null){
            return false;
        } else {
            return true;
        }
    }
}