class Mammal < Animal
  def initialize(weight, is_terrestrial)
    super(weight)
    @is_terrestrial = is_terrestrial
  end

  def nurse
    puts "I'm breastfeeding"
  end
end
