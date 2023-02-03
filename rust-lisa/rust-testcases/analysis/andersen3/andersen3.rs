fn main() {
	let mut x : i32 = 5;
	let p = &mut x;
	
	loop {
		*p += 1
	}
}
