import Foundation

class MockProvider {
    static let shared = MockProvider()
    
    let user = UserProfile(
        name: "Ahmet Bey",
        totalWaterCredit: 5000.0,
        usedWaterCredit: 3500.0,
        region: "Konya / Karapınar"
    )
    
    let fields = [
        Field(name: "Kuzey Tarlası", area: 45.0, currentCrop: "Mısır", waterConsumption: 1200.0, healthStatus: "İyi"),
        Field(name: "Yol Kenarı", area: 20.0, currentCrop: "Şeker Pancarı", waterConsumption: 800.0, healthStatus: "Kritik"),
        Field(name: "Tepe Mevkii", area: 30.0, currentCrop: "Arpa", waterConsumption: 500.0, healthStatus: "Normal")
    ]
}
