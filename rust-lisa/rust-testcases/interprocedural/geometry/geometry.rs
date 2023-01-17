trait Measures {
	fn area(&self) -> i32;
	fn perimeter(&self) -> i32;
}

struct Circle {
	r: i32,
	x: i32,
	y: i32
}

impl Measures for Circle {
	fn area(&self) -> i32 {
		self.r * self.r
	}
	
	fn perimeter(&self) -> i32 {
		2 * self.r * 3
	}
}

struct Square {
	x: i32,
	y: i32,
	l: i32
}

impl Measures for Square {
	fn area(&self) -> i32 {
		self.l * self.l
	}
	
	fn perimeter(&self) -> i32 {
		4 * self.l
	}
}

struct Rectangle {
	x: i32,
	y: i32,
	w: i32,
	h: i32
}


impl Measures for Rectangle {
	fn area(&self) -> i32 {
		self.w * self.h
	}
	
	fn perimeter(&self) -> i32 {
		2 * self.w + 2 * self.h
	}
}

impl Rectangle {
    fn new(x: i32, y: i32, width: i32, height: i32) -> Rectangle {
        Rectangle {
            x,
            y,
            w : width,
            h : height,
        }
    }
    
}

fn main() {
	let c = Circle {x : 1, y : 1, r : 1};
	let c_p = c.perimeter();
	let c_a = c.area();
	let _circle_asp = c.area() + c.perimeter();
	let s = Square {x : 1, y : 1, l : 1};
	let _square_asp = s.area() + s.perimeter();
	let r = Rectangle::new(1, 1, 1, 1);
	let _rectangle_asp = r.area() + r.perimeter();
}
