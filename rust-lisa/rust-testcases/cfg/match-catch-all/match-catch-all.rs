fn main() {
    let x = 2;

	match x {
        _ => println!("I do not know what x is"),
    }
    
    match x {
    	2 => println!("x is definitely 2"),
        _ => println!("x is not 2 nor 3 nor 4!"),
    }
    
    match x {
    	_ if x == 3 => println!("x is 3"),
    }
    
    match x {
    	3 => println!("x is 3"),
    	_ => println!("x is not 3"),
    	4 => println!("x is probably 4"),
    }
}
