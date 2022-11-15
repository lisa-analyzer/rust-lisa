struct PointOne { }

struct PointTwo {
	x : i32,
	y : i32,
}

fn main() {
	let point_one = PointOne {};
	let point_two = PointTwo { x : 1, y : 2 };
	
	point_two.x = -3;
}