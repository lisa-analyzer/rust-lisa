fn main() {
	let x = 5;
	let y = 7;
	let z = 9;
	
	let mut p = &x;
	if true {
		p = &y;
	} else {
		p = &z;
	}
}
