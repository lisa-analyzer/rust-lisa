fn main() {
	let mut x : i32 = 5;
	let p = &mut x;
	
	while true {
		*p += 1
	}
}
