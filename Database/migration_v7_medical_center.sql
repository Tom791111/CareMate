USE caremate_db;

CREATE TABLE IF NOT EXISTS appointment (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT DEFAULT 1,
    hospital_name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    doctor_name VARCHAR(50),
    appointment_date DATE,
    appointment_time VARCHAR(30),
    visit_reason VARCHAR(100),
    status VARCHAR(30) DEFAULT '已預約',
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS favorite_hospital (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT DEFAULT 1,
    hospital_name VARCHAR(100) NOT NULL,
    type VARCHAR(30),
    address VARCHAR(255),
    phone VARCHAR(30),
    map_keyword VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS medical_notice (
    notice_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT DEFAULT 1,
    title VARCHAR(100),
    content TEXT,
    notice_time DATETIME,
    status VARCHAR(30) DEFAULT '未提醒',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hospital_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT DEFAULT 1,
    hospital_name VARCHAR(100),
    department VARCHAR(50),
    doctor_name VARCHAR(50),
    visit_date DATE,
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
