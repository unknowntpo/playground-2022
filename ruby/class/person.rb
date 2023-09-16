class Person
  attr_accessor :name, :age
  # Constructor method
  def initialize(name, age)
    @name = name
    @age = age
  end

  # Instance method
  def introduce
    puts "Hi, my name is #{@name}, and I am #{@age} years old."
  end
end

# Creating an instance of the Person class
person1 = Person.new("Alice", 30)

# Accessing instance variables and calling methods
puts "Name: #{person1.name}"
puts "Age: #{person1.age}"
person1.introduce

# Updating instance variables using setters
person1.name = "Bob"
person1.age = 25

# Calling the introduce method again
person1.introduce
