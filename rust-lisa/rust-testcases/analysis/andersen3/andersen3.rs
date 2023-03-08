fn main() {
	let mut x = 5;
	let p = &mut x;
	
	loop {
		*p += 1;
	}
}
