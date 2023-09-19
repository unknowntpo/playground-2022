class Parkings < ApplicationRecord
  validates_presence_of :parking_type, :start_at
  validates_inclusion_of :parking_type, :in => ["guest", "short-term", "long-term" ]   

  validate :validate_end_at_with_amount

  def validate_end_at_with_amount
    if (end_at.present? && amount.blank?)
      errors.add(:amount, "if end_at presents, then amount should presents")
    end

    if (end_at.blank? && amount.present?)
      errors.add(:amount, "if amount presents, then end_at should presents")
    end
  end
end
