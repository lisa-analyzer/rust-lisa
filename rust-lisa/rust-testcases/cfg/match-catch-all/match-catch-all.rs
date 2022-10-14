fn main() {
    let x = 2;
    
	match x {
        _ => println!("I do not know what x is"),
    }
    
    match x {
    	2 => println!("x is definitely 2"),
        _ => println!("x is not 2!"),
    }
    
    match x {
    	_ if x == 3 => println!("x is 3"),
    }
    
    match x {
    	_ if x == 3 => println!("x is 3"),
    	_ if x == 2 => println!("x is 2"),
    	_ if x == 1 => println!("x is 1"),
    	_ if x == 0 => println!("x is 0"),
    	_ => println!("x is unknown"),
    }
    
    match x {
    	10 => println!("x is 10"),
    	_ => println!("x is not 3"),
    	4 => println!("x is probably 4"),
    }
    
    match x {
    	3 => println!("x is 3"),
    	_ => println!("x is not 3"),
    	5 => println!("x is 5"),
    	_ => println!("x is not known"),
    	4 => println!("x is probably 4"),
    }
    
    match x {
        2 | _ if x < 5 => println!("x is 2 or 3 or 4"),
        _ => println!("x is not 2 nor 3 nor 4!"),
    }
}
