package com.smartenergy;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class EnergyController {
    private final List<Device> devices = new ArrayList<>(List.of(
        new Device(1, "Living Room Lights", "Lighting", 85, "ON"),
        new Device(2, "Air Conditioner", "Cooling", 1250, "ON"),
        new Device(3, "Refrigerator", "Appliance", 180, "ON"),
        new Device(4, "Computer", "Electronics", 220, "OFF"),
        new Device(5, "Water Heater", "Heating", 1500, "OFF")
    ));
    private double todayKwh = 6.42;
    private double monthKwh = 142.8;
    private double tariff = 8.0;
    private final Random random = new Random();

    @GetMapping("/api/dashboard")
    public synchronized Map<String, Object> dashboard() {
        int load = devices.stream().filter(d -> d.getStatus().equals("ON"))
                .mapToInt(Device::getWatts).sum();
        int power = Math.max(0, load + random.nextInt(51) - 25);
        // Demo simulation: adds approximately 5 seconds of consumption per refresh.
        todayKwh += power / 1000.0 / 720.0;
        monthKwh += power / 1000.0 / 720.0;

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("power_w", power);
        summary.put("today_kwh", round(todayKwh));
        summary.put("month_kwh", round(monthKwh));
        summary.put("cost_today", round(todayKwh * tariff));
        summary.put("carbon_kg", round(todayKwh * 0.5));
        summary.put("updated_at", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summary", summary);
        result.put("devices", new ArrayList<>(devices));
        result.put("recommendations", recommendations(power));
        return result;
    }

    @PostMapping("/api/devices/{id}/toggle")
    public synchronized Map<String, Object> toggleDevice(@PathVariable int id) {
        Device device = devices.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
        if (device == null) throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Device not found");
        device.setStatus(device.getStatus().equals("ON") ? "OFF" : "ON");
        return Map.of("message", device.getName() + " turned " + device.getStatus(), "device", device);
    }

    @GetMapping("/api/usage")
    public Map<String, Object> usage() {
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int ago = 6; ago >= 0; ago--) {
            labels.add(now.minusDays(ago).format(DateTimeFormatter.ofPattern("EEE")));
            values.add(Math.round((4.2 + random.nextDouble() * 5.6) * 100.0) / 100.0);
        }
        return Map.of("labels", labels, "values", values);
    }

    @GetMapping("/api/recommendations")
    public List<Map<String, String>> getRecommendations() {
        return recommendations(0);
    }

    private List<Map<String, String>> recommendations(int power) {
        List<Map<String, String>> tips = new ArrayList<>();
        if (power > 2000) tips.add(tip("High power consumption", "Current load is above 2 kW. Check high-power devices.", "Review active devices"));
        tips.add(tip("Optimize air conditioning", "Try setting the AC between 24°C and 26°C and clean the filter regularly.", "Potentially reduce cooling energy"));
        tips.add(tip("Switch off idle devices", "Turn off lights and computers when they are not needed.", "Avoid unnecessary consumption"));
        tips.add(tip("Use appliances thoughtfully", "Run flexible high-power appliances during lower-tariff periods when available.", "Potential cost savings"));
        return tips;
    }

    private Map<String, String> tip(String title, String detail, String saving) {
        return Map.of("title", title, "detail", detail, "saving", saving);
    }
    private double round(double value) { return Math.round(value * 100.0) / 100.0; }
}
