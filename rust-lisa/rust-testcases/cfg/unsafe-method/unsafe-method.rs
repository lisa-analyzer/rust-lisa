struct Number {
	n : i32,
}

impl Number {
	unsafe fn get(&self) -> i32 {
		self.n
	}
}

fn main() {
    let number = Number { n : 1 };

    unsafe {
        number.get();
    }
}
