fn main() {
	let mut num = 5;
	let r1 = &num as *const i32;
	/*
	let r2 = &mut num as *mut i32;
	unsafe {
		let n1 = *r1;
		(*r2) += 1;
		let n2 = *r2;
		println! ("r1 is : {:?}", n1);
		println! ("r2 is : {:?}", n2);
	}
	*/
}
