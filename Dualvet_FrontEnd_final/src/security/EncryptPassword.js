// src/security/EncryptPassword.js
import CryptoJS from 'crypto-js';

export const EncryptPassword = (password) => {
  const salt = CryptoJS.lib.WordArray.random(128 / 8).toString();
  const iterations = 1000;
  const keySize = 256 / 32;
  const hashedPassword = CryptoJS.PBKDF2(password, salt, {
    keySize: keySize,
    iterations: iterations,
  }).toString();

  return {
    salt: salt,
    hashedPassword: hashedPassword,
  };
};
