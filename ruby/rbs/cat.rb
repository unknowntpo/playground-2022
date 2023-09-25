class Cat < Mammal
  def initialize(weight, n_of_lives, is_terrestrial: true)
    super(weight, is_terrestrial)
    @n_of_lives = n_of_lives
  end

  def speak
    puts 'Meow'
  end
end
