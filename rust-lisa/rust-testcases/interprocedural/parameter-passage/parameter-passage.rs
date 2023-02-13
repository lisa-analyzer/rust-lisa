struct Rectangle {
	h : i32, 
	w : i32
}

// Inspired by
// https://doc.rust-lang.org/book/ch04-01-what-is-ownership.html

fn takes_ownership(_some_rectangle: Rectangle) {
    ()
}

fn makes_copy(some_integer: i32) -> i32 {
    some_integer * 3
}

fn gives_ownership() -> Rectangle {
    let some_rectangle = Rectangle { h: 1, w: 1 };
    some_rectangle
}

fn takes_and_new(_a_rectangle: Rectangle) -> Rectangle {
	Rectangle { h: 0, w: 0 }
}

fn do_not_take_ownership(r: &Rectangle) -> i32 {
	r.h + r.w
}

fn main() {
    let r1 = gives_ownership();
    let area = do_not_take_ownership(&r1);
    let r2 = takes_and_new(r1);
    takes_ownership(r2);
    let _b = makes_copy(area);
}
