import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            // 1. Ana Ekran
            DashboardView()
                .tabItem {
                    Label("Özet", systemImage: "drop.fill")
                }
            
            // 2. Tarlalarım (Şimdilik Boş Yer Tutucu)
            Text("Tarlalarım Ekranı Yapım Aşamasında...")
                .tabItem {
                    Label("Tarlalar", systemImage: "leaf.fill")
                }
            
            // 3. NLP Sesli Asistan
            Text("Akıllı Asistan Yapım Aşamasında...")
                .tabItem {
                    Label("Asistan", systemImage: "waveform.circle.fill")
                }
            
            // 4. Kuantum Optimizasyon Sonuçları
            Text("Optimizasyon Paneli...")
                .tabItem {
                    Label("Analiz", systemImage: "chart.xyaxis.line")
                }
        }
        .tint(.blue) // DropWise temasına uygun su mavisi
    }
}

#Preview {
    ContentView()
}
