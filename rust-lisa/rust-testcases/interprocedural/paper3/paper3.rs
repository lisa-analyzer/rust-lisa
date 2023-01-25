///////////////////  Boilerplate Code  /////////////////////////////////////
struct slice {
	// Blank
}

impl slice {
	fn from_raw_parts_mut(_s : *mut i32, _m : usize) -> &mut [i32] {
		s as &mut [i32]
	}
}
////////////////////////////////////////////////////////////////////////////

fn split_at_mut(values: &mut [i32], mid: usize) -> (&mut [i32], &mut [i32]) {
	let len = values.len();
	let ptr = values.as_mut_ptr();
	
	assert!(mid <= len);
	
	unsafe {
		(
			slice::from_raw_parts_mut(ptr, mid),
			slice::from_raw_parts_mut(ptr.add(mid), len - mid),
		)
	}
}
