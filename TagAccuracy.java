package com.testing.app.my_test2;

public class TagAccuracy {
	double Tpositive;
	double Fpositive;
	double Fnegative;
	
	TagAccuracy(){
		Tpositive = 0;
		Fpositive = 0;
		Fnegative = 0;
	}
	
	TagAccuracy incrementTp(){
		Tpositive += 1;
		return this; 
	}
	
	TagAccuracy incrementFn(){
		Fnegative += 1;
		return this;
	}
	
	TagAccuracy incrementFp(){
		Fpositive += 1;
		return this;
	}
	
	double precision(){
		
		double p = (this.Tpositive + 1)/(this.Tpositive + this.Fpositive + 1);
		return p;
	}
	
	double recall(){
		double r = (this.Tpositive + 1)/(this.Tpositive + this.Fnegative + 1);
		return r;
	}
	double f1score(){
		System.out.println(this.Tpositive);
		System.out.println(this.Fnegative);
		System.out.println(this.Fpositive);
		System.out.println(this.precision());
		System.out.println(this.recall());
		double f = 2*((this.precision()*this.recall())/(this.precision() + this.recall()));
		return f;
	}

}
