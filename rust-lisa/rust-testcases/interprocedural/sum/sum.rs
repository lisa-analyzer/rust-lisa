fn id(x: i32) -> i32 {
	x
}

fn sum(x: i32, y: i32) -> i32 {
	return id(x + y);
}

fn main() {
	let a = sum(3, 2);
}
