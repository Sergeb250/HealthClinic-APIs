# Clinic Management System - Spring Boot

A comprehensive clinic management system built with Spring Boot that uses **DTOs (Data Transfer Objects)** for clean separation between database entities and API responses. The system supports doctors, patients, appointments, and location hierarchy management.

## 🏗️ Architecture

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│         Controllers (API)           │  ← REST endpoints, accept/return DTOs
├─────────────────────────────────────┤
│         Services (Logic)            │  ← Business logic, DTO ↔ Entity conversion
├─────────────────────────────────────┤
│      Repositories (Data Access)     │  ← JPA queries, work with Entities
├─────────────────────────────────────┤
│         Entities (Database)         │  ← Database models (not exposed)
└─────────────────────────────────────┘
```

### Key Principles

- **No Entity Exposure**: Entities are never returned directly from controllers
- **DTO Conversion**: All data transformation happens in the service layer
- **Nested Data**: DTOs include related data (appointments, location) without circular references
- **Search & Filter**: Enhanced query capabilities with location-based filtering

---

## 📦 Entity Relationships

```
Person ──┬──> Village ──> Cell ──> Sector ──> District ──> Province
         │
         └──> Doctor ──┬──> Appointment <──┬──> Patient ──> Village
                       │                    │
                       └────────────────────┘
```

- **Person**: Base entity with location (village)
- **Doctor**: Extends Person, has specialization and appointments
- **Patient**: Has location (village) and appointments
- **Appointment**: Links Doctor and Patient with date/status
- **Location Hierarchy**: Province → District → Sector → Cell → Village

---

## 🎯 DTOs Structure

### 1. **DoctorDTO**
Full doctor information with appointments and address.
```json
{
  "id": 1,
  "name": "Dr. Muhire Innocent",
  "specialization": "Cardiology",
  "phone": "+250788123456",
  "email": "muhire.doc@clinic.rw",
  "address": {
    "provinceId": 1,
    "provinceName": "Kigali",
    "districtId": 2,
    "districtName": "Gasabo",
    "sectorId": 3,
    "sectorName": "Gisozi",
    "cellId": 4,
    "cellName": "Kacyiru",
    "villageId": 5,
    "villageName": "Village A"
  },
  "appointments": [
    {
      "id": 1,
      "date": "2025-11-10T14:30:00",
      "patientName": "serge benit",
      "status": "SCHEDULED"
    }
  ]
}
```

### 2. **DoctorMinimalDTO**
Lightweight doctor info (for lists/searches).
```json
{
  "id": 1,
  "name": "Dr.  muhire innocent",
  "specialization": "Cardiology",
  "address": { /* full location hierarchy */ }
}
```

### 3. **PatientDTO**
Patient information with age calculation, location, and appointments.
```json
{
  "id": 1,
  "name": "Kevine",
  "age": 31,
  "dob": "1993-05-15",
  "gender": "Female",
  "phone": "+250788654321",
  "email": "patient@example.com",
  "location": { /* full location hierarchy */ },
  "appointments": [
    {
      "id": 1,
      "date": "2025-11-10T14:30:00",
      "doctorName": "Dr. Igizeneza",
      "status": "SCHEDULED"
    }
  ]
}
```

### 4. **AppointmentDTO**
Appointment with minimal nested doctor/patient info.
```json
{
  "id": 1,
  "date": "2025-11-10T14:30:00",
  "doctorName": "Dr. Igizeneza",
  "patientName": "Kevine",
  "status": "SCHEDULED",
  "notes": "Follow-up checkup"
}
```

### 5. **AppointmentSummaryDTO**
Compact appointment representation (used in nested lists).
```json
{
  "id": 1,
  "date": "2025-11-10T14:30:00",
  "patientName": "Eric",
  "doctorName": "Dr. Muhire Innocent",
  "status": "SCHEDULED"
}
```

### 6. **PersonDTO**
Person with location hierarchy.
```json
{
  "id": 1,
  "name": "sergebenit",
  "email": "serge@gmail.com",
  "phone": "+250788111222",
  "location": { /* full location hierarchy */ }
}
```

### 7. **LocationDTO**
Complete location hierarchy from province to village.
```json
{
  "provinceId": 1,
  "provinceName": "Kigali",
  "districtId": 2,
  "districtName": "Gasabo",
  "sectorId": 3,
  "sectorName": "Gisozi",
  "cellId": 4,
  "cellName": "Kacyiru",
  "villageId": 5,
  "villageName": "Village A"
}
```

---

## 🔗 API Endpoints

### **Doctors API** (`/api/doctors`)

| Method | Endpoint | Description | Returns |
|--------|----------|-------------|---------|
| `GET` | `/api/doctors` | Get all doctors | `List<DoctorDTO>` |
| `GET` | `/api/doctors?page=0&size=10&sortBy=name` | Get doctors (paginated) | `List<DoctorDTO>` |
| `GET` | `/api/doctors/{id}` | Get doctor by ID | `DoctorDTO` |
| `GET` | `/api/doctors/search/name?name={name}` | Search doctors by name | `List<DoctorMinimalDTO>` |
| `GET` | `/api/doctors/search/specialization?specialization={spec}` | Search by specialization | `List<DoctorMinimalDTO>` |
| `GET` | `/api/doctors/specialization/{specialization}` | List by exact specialization | `List<DoctorMinimalDTO>` |
| `POST` | `/api/doctors` | Create new doctor | `DoctorDTO` |
| `PUT` | `/api/doctors/{id}` | Update doctor | `DoctorDTO` |
| `DELETE` | `/api/doctors/{id}` | Delete doctor | `204 No Content` |

**Example: Search doctors by specialization**
```bash
GET /api/doctors?specialization=Cardiology
```

---

### **Patients API** (`/api/patients`)

| Method | Endpoint | Description | Returns |
|--------|----------|-------------|---------|
| `GET` | `/api/patients` | Get all patients | `List<PatientDTO>` |
| `GET` | `/api/patients?page=0&size=10&sortBy=name` | Get patients (paginated) | `List<PatientDTO>` |
| `GET` | `/api/patients/{id}` | Get patient by ID (with location & appointments) | `PatientDTO` |
| `GET` | `/api/patients/gender/{gender}` | Filter by gender | `List<PatientDTO>` |
| `GET` | `/api/patients/location/province/{provinceId}` | Filter by province | `List<PatientDTO>` |
| `GET` | `/api/patients/location/district/{districtId}` | Filter by district | `List<PatientDTO>` |
| `GET` | `/api/patients/location/sector/{sectorId}` | Filter by sector | `List<PatientDTO>` |
| `POST` | `/api/patients` | Create new patient | `PatientDTO` |
| `PUT` | `/api/patients/{id}` | Update patient | `PatientDTO` |
| `DELETE` | `/api/patients/{id}` | Delete patient | `204 No Content` |

**Example: Get patient with full nested data**
```bash
GET /api/patients/1
```
Returns patient with:
- Calculated age
- Full location hierarchy
- List of appointments with doctor names

---

### **Appointments API** (`/api/appointments`)

| Method | Endpoint | Description | Returns |
|--------|----------|-------------|---------|
| `GET` | `/api/appointments` | Get all appointments | `List<AppointmentDTO>` |
| `GET` | `/api/appointments?page=0&size=10` | Get appointments (paginated) | `List<AppointmentDTO>` |
| `GET` | `/api/appointments/{id}` | Get appointment by ID | `AppointmentDTO` |
| `GET` | `/api/appointments/status/{status}` | Filter by status (e.g., SCHEDULED) | `List<AppointmentDTO>` |
| `GET` | `/api/appointments/doctor/{doctorId}` | Get doctor's appointments | `List<AppointmentDTO>` |
| `GET` | `/api/appointments/patient/{patientId}` | Get patient's appointments | `List<AppointmentDTO>` |
| `POST` | `/api/appointments` | Create new appointment | `AppointmentDTO` |
| `PUT` | `/api/appointments/{id}` | Update appointment | `AppointmentDTO` |
| `DELETE` | `/api/appointments/{id}` | Delete appointment | `204 No Content` |

**Example: Create appointment**
```bash
POST /api/appointments
Content-Type: application/json

{
  "date": "2025-11-10T14:30:00",
  "doctorName": "Dr. Igizeneza",
  "patientName": "Kevine",
  "status": "SCHEDULED",
  "notes": "Annual checkup"
}
```

---

### **Persons API** (`/api/persons`)

| Method | Endpoint | Description | Returns |
|--------|----------|-------------|---------|
| `GET` | `/api/persons` | Get all persons | `List<PersonDTO>` |
| `GET` | `/api/persons?page=0&size=10` | Get persons (paginated) | `List<PersonDTO>` |
| `GET` | `/api/persons/{id}` | Get person by ID | `PersonDTO` |
| `GET` | `/api/persons/province/{provinceName}` | Search by province name | `List<PersonDTO>` |
| `GET` | `/api/persons/province/code/{provinceCode}` | Search by province code | `List<PersonDTO>` |
| `POST` | `/api/persons` | Create new person | `PersonDTO` |
| `PUT` | `/api/persons/{id}` | Update person | `PersonDTO` |
| `DELETE` | `/api/persons/{id}` | Delete person | `204 No Content` |

---

## 🛠️ Service Layer Conversion

All services implement conversion between Entities and DTOs:

### DoctorService
- `convertToDTO(Doctor)` → `DoctorDTO`
- `convertToMinimalDTO(Doctor)` → `DoctorMinimalDTO`
- `convertToEntity(DoctorDTO)` → `Doctor`

### PatientService
- `convertToDTO(Patient)` → `PatientDTO` (includes age calculation)
- `convertToEntity(PatientDTO)` → `Patient`

### AppointmentService
- `convertToDTO(Appointment)` → `AppointmentDTO`
- `convertToEntity(AppointmentDTO)` → `Appointment`

### PersonService
- `convertToDTO(Person)` → `PersonDTO`
- `convertToEntity(PersonDTO)` → `Person`

All services include a **shared helper**:
```java
private LocationDTO convertVillageToLocation(Village village)
```
This recursively builds the full location hierarchy (Province → District → Sector → Cell → Village).

---





2. **Configure database** (`application.properties`)
```properties
spring.datasource.url=jdbc:postgres://localhost:5000/clinic_db
spring.datasource.username=postgres
spring.datasource.password=1243
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

4. **Access the API**
```
http://localhost:8080/api/doctors
http://localhost:8080/api/patients
http://localhost:8080/api/appointments
```

---

## 📊 Database Schema Highlights

### Key Tables
- `doctors` (id, name, specialization, phone, email, person_id)
- `patients` (id, name, dob, gender, phone, email, village_id)
- `appointments` (id, appointment_date, status, notes, doctor_id, patient_id)
- `persons` (id, name, email, phone, village_id)
- `provinces`, `districts`, `sectors`, `cells`, `villages` (location hierarchy)

### Important Relationships
- **Doctor ↔ Person**: One-to-One (via `person_id`)
- **Patient ↔ Village**: Many-to-One (via `village_id`)
- **Appointment ↔ Doctor/Patient**: Many-to-One

---

## ✨ Key Features

### 1. **No @JsonIgnore Required**
Entities are never exposed, so no need for `@JsonIgnore` annotations. All circular reference issues are handled via DTOs.

### 2. **Automatic Age Calculation**
`PatientDTO` calculates age from date of birth automatically.

### 3. **Location Hierarchy**
Full province-to-village hierarchy included in every response where applicable.

### 4. **Appointment Summaries**
Nested appointment lists show minimal info (id, date, name, status) to avoid bloat.

### 5. **Enhanced Search**
- Search doctors by name or specialization
- Filter patients by gender or location hierarchy
- Filter appointments by status, doctor, or patient

### 6. **Pagination Support**
All list endpoints support `?page=0&size=10&sortBy=fieldName` parameters.

---

## 🧪 Testing

### Example API Calls

**Get all doctors with pagination:**
```bash
curl http://localhost:8080/api/doctors?page=0&size=5&sortBy=name
```

**Search doctors by specialization:**
```bash
curl http://localhost:8080/api/doctors/search/specialization?specialization=Cardio
```

**Get patient with full nested data:**
```bash
curl http://localhost:8080/api/patients/1
```

**Filter patients by province:**
```bash
curl http://localhost:8080/api/patients/location/province/1
```

**Create appointment:**
```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-12-01T10:00:00",
    "doctorName": "Dr. Serge",
    "patientName": "Josiane",
    "status": "SCHEDULED"
  }'
```

---

## 📝 Development Notes

### Adding New DTOs
1. Create DTO class in `dto` package
2. Add conversion methods in corresponding service
3. Update controller to use new DTO

### Adding New Search Endpoints
1. Add query method to repository (e.g., `findByNameContaining`)
2. Implement service method using the query
3. Add controller endpoint calling the service

---

## 🎯 Project Structure

```
clinic-system/
├── src/main/java/com/auca/clinic_system/
│   ├── dto/                # DTO classes
│   │   ├── DoctorDTO.java
│   │   ├── DoctorMinimalDTO.java
│   │   ├── PatientDTO.java
│   │   ├── AppointmentDTO.java
│   │   ├── AppointmentSummaryDTO.java
│   │   ├── LocationDTO.java
│   │   └── PersonDTO.java
│   ├── entity/             # Entity classes
│   ├── repository/         # JPA repositories
│   ├── service/            # Business logic + DTO conversion
│   │   ├── DoctorService.java
│   │   ├── PatientService.java
│   │   ├── AppointmentService.java
│   │   └── PersonService.java
│   ├── controller/         # REST controllers
│   └── ClinicSystemApplication.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

---

## 📄 License

This project is for educational purposes.

---



Built with Spring Boot, JPA, Lombok, and MySQL/PostgreSQL.
