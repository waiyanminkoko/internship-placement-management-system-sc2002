# Resources Directory

## Overview

This directory contains application configuration and data files for the Internship Placement Management System.

## Contents

### `application.properties`
Spring Boot configuration file containing:
- Server port configuration (port 6060)
- Application settings
- Logging configuration
- CSV file path settings

### `data/` Folder
Contains CSV files used for data persistence:

| File | Purpose |
|------|---------|
| `student_list.csv` | Student account data (ID, name, password, major, year, applications) |
| `company_representative_list.csv` | Company representative accounts (ID, name, company, email, status) |
| `staff_list.csv` | Career center staff accounts (ID, name, department) |
| `internship_opportunities.csv` | Internship postings created by company representatives |
| `applications.csv` | Student applications to internships |
| `withdrawal_requests.csv` | Withdrawal requests from students |

**Note**: CSV files serve as the application's database. All data is loaded on startup and saved directly to these files when changes occur.

## Data Persistence

The application reads from and writes to `src/main/resources/data/` as configured in `application.properties`:
```
csv.data.path=src/main/resources/data/
```

All CRUD operations modify these CSV files directly, making them the single source of truth for the application's data.

---

**Last Updated**: November 16, 2025
