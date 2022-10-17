fn main() {
    let mut a = 1;
    unsafe {
        while a < 5 {
            a = a + 1;
        }
    }
    println!("{}", a);
    
    let a_ptr: *const i32 = &a;
    unsafe {
        println!("{}", *a_ptr)
    }
}
