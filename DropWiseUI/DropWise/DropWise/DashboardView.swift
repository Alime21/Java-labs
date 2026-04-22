import SwiftUI

struct DashboardView: View {
    let userData = MockProvider.shared.user
    // Şimdilik görsel için sahte (mock) durum değişkenleri
    //@State private var waterCreditPercentage: CGFloat = 0.70 // %70 dolu
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    
                    // 1. Karşılama Kartı
                    HStack {
                        VStack(alignment: .leading, spacing: 4) {
                            Text("Hoş Geldin,")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                            Text(userData.name)
                                .font(.title2)
                                .bold()
                        }
                        Spacer()
                        Image(systemName: "person.crop.circle.fill")
                            .resizable()
                            .frame(width: 45, height: 45)
                            .foregroundColor(.blue)
                    }
                    .padding()
                    .background(Color(.systemBackground))
                    .cornerRadius(16)
                    .shadow(color: Color.black.opacity(0.05), radius: 8, x: 0, y: 4)
                    
                    // 2. Su Kredisi Dairesel Göstergesi (Circular Progress Bar)
                    VStack {
                        Text("Kalan Su Kredisi")
                            .font(.headline)
                            .padding(.bottom, 10)
                        
                        ZStack {
                            // Arka plan soluk halka
                            Circle()
                                .stroke(lineWidth: 20)
                                .opacity(0.2)
                                .foregroundColor(.blue)
                            
                            // Dolu olan hareketli halka
                            Circle()
                                .trim(from: 0.0, to: userData.usagePercentage)
                                .stroke(style: StrokeStyle(lineWidth: 20, lineCap: .round, lineJoin: .round))
                                .foregroundColor(.blue)
                                .rotationEffect(Angle(degrees: 270.0))
                                .animation(.easeInOut(duration: 1.5), value: userData.usagePercentage)
                            
                            // Ortadaki Yazı
                            VStack {
                                Text("\(Int(userData.usagePercentage * 100))%")
                                    .font(.system(size: 40, weight: .bold, design: .rounded))
                                    .foregroundColor(.primary)
                                Text("Kullanılabilir")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                        }
                        .frame(width: 180, height: 180)
                        .padding()
                    }
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color(.systemBackground))
                    .cornerRadius(16)
                    .shadow(color: Color.black.opacity(0.05), radius: 8, x: 0, y: 4)
                    
                    // 3. Mini Bildirim Kartı
                    HStack(spacing: 15) {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .foregroundColor(.orange)
                            .font(.title2)
                        
                        VStack(alignment: .leading) {
                            Text("Kuraklık Uyarısı")
                                .font(.subheadline)
                                .bold()
                            Text("Bölgenizde yağışlar bu ay %15 altında.")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                        Spacer()
                    }
                    .padding()
                    .background(Color.orange.opacity(0.1))
                    .cornerRadius(12)
                    
                    Spacer()
                }
                .padding()
            }
            .navigationTitle("DropWise")
            .background(Color(.systemGroupedBackground))
        }
    }
}

#Preview {
    DashboardView()
}
