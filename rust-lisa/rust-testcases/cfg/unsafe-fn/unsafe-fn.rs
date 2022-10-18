unsafe fn times2(a : i32) -> i32 {
    a * 2
}

unsafe fn difficult_fn(b : i32) -> i32 {
    if b == 2 {
        return 3;
    }
    
    10
}

fn main() {
    unsafe {
        let _y = times2(3);
    
        let _z = difficult_fn(3);
    }
}
