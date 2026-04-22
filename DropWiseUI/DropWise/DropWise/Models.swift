import Foundation

// Tarlayı temsil eden model
struct Field: Identifiable, Codable {
    var id = UUID()
    let name: String
    let area: Double // Dekar cinsinden
    let currentCrop: String
    let waterConsumption: Double // Harcanan su
    let healthStatus: String // "İyi", "Kritik", "Normal"
}

// Çiftçi profilini temsil eden model
struct UserProfile: Codable {
    let name: String
    let totalWaterCredit: Double // Toplam hak
    let usedWaterCredit: Double // Kullanılan miktar
    let region: String
    
    var usagePercentage: Double {
        return usedWaterCredit / totalWaterCredit
    }
}
