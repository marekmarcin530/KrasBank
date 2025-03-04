-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 08 Maj 2023, 20:18
-- Wersja serwera: 10.4.27-MariaDB
-- Wersja PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `bank`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `operacje`
--

CREATE TABLE `operacje` (
  `id_operacji` int(11) NOT NULL,
  `typ_operacji` varchar(255) NOT NULL,
  `id_nadawcy` int(255) NOT NULL,
  `id_odbiorcy` int(255) DEFAULT NULL,
  `kwota` int(255) NOT NULL,
  `data` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `operacje`
--

INSERT INTO `operacje` (`id_operacji`, `typ_operacji`, `id_nadawcy`, `id_odbiorcy`, `kwota`, `data`) VALUES
(1, 'Zasilenie własne', 1, 1, 75, '2023-05-07 17:17:13'),
(2, 'Przelew do 123456789 od 987654321', 1, 2, 25, '2023-05-07 17:17:22'),
(3, 'Przelew do 987654321 od 123456789', 2, 1, 25, '2023-05-07 17:18:00'),
(4, 'Przelew do 987654321 od 123456789', 2, 1, 28, '2023-05-07 17:18:22'),
(5, 'Zasilenie własne', 1, 1, 7, '2023-05-08 17:49:18');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uzytkownicy`
--

CREATE TABLE `uzytkownicy` (
  `Id` int(255) NOT NULL,
  `login` varchar(255) NOT NULL,
  `haslo` varchar(255) NOT NULL,
  `imie` varchar(255) NOT NULL,
  `nazwisko` varchar(255) NOT NULL,
  `adres` varchar(255) NOT NULL,
  `nr_telefonu` int(11) NOT NULL,
  `bilans` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `uzytkownicy`
--

INSERT INTO `uzytkownicy` (`Id`, `login`, `haslo`, `imie`, `nazwisko`, `adres`, `nr_telefonu`, `bilans`) VALUES
(1, 'admin', 'admin', 'admin', 'admin', 'Jarosław', 123456789, 210),
(2, 'user', 'user', 'user', 'user', 'Jarosław', 987654321, 72);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `operacje`
--
ALTER TABLE `operacje`
  ADD PRIMARY KEY (`id_operacji`),
  ADD KEY `id_nadawcy` (`id_nadawcy`),
  ADD KEY `id_odbiorcy` (`id_odbiorcy`);

--
-- Indeksy dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `nr_telefonu` (`nr_telefonu`),
  ADD UNIQUE KEY `login` (`login`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `operacje`
--
ALTER TABLE `operacje`
  MODIFY `id_operacji` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  MODIFY `Id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `operacje`
--
ALTER TABLE `operacje`
  ADD CONSTRAINT `operacje_ibfk_1` FOREIGN KEY (`id_nadawcy`) REFERENCES `uzytkownicy` (`Id`),
  ADD CONSTRAINT `operacje_ibfk_2` FOREIGN KEY (`id_odbiorcy`) REFERENCES `uzytkownicy` (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
