struct PointOne { }

struct PointTwo {
	x : i32,
	y : i32,
}

impl PointTwo {
	fn reverse(&mut self) {
		let a = self.x;
		
		self.x = self.y;
		self.y = self.x;
	}
}

fn main() {
	let mut point_one = PointOne {};
	let mut point_two = PointTwo { x : 1, y : 2 };
	
	point_two.x = -3;
	point_two.y += 1;
}
