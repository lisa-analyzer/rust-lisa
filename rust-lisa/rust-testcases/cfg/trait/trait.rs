trait Measure {
    fn area(&self) -> f32;
    fn perimeter(&self) -> f32;
}

struct Circle {
    x: f32,
    y: f32,
    r: f32,
}

impl Circle {
    pub fn origin_distance_squared(&self) -> f32 {
        self.x * self.x + self.y * self.y
    }
}

impl Measure for Circle {
    fn area(&self) -> f32 {
        self.r * self.r * 3.14
    }
    
    fn perimeter(&self) -> f32 {
        2.0 * self.r * 3.14
    }
}

fn main() {
    let circle = Circle { x : 0.0, y : 0.0, r : 1.0 };
    
    println!("{}", circle.area());
    println!("{}", circle.perimeter());
    println!("{}", circle.origin_distance_squared());
}
