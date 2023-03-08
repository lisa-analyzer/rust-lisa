struct PointOne { }

struct PointTwo {
	x : i32,
	y : i32,
}

fn main() {
	let mut point_one = PointOne {};
	let mut point_two = PointTwo { x : 1, y : 2 };
	
	point_two.x = -3;
	point_two.y += 1;
}
