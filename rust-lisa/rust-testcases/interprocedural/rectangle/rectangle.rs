struct Rectangle {
    width : usize,
    height : usize,
}

impl Rectangle {
	fn area(&self) -> usize {
		self.width * self.height
	}
}

fn area(x : Rectangle) -> usize {
	x.width * x.height
}

fn main() {
    let r = Rectangle { width: 4, height: 5};    
    let _a = area(r); //passato per valore e poi distrutto
    
    let _b = r.area();
}
