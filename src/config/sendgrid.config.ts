import {EmptyConfig} from './empty.config';

export const SENDGRID_CONFIG = 'config.Sendgrid'

export interface SendgridConfig {
  apiKey: string;
  valid: boolean;
  fromEmail: string;
  channel: string;
}

const buildSendgridConfig = (): SendgridConfig | EmptyConfig => {
  const apiKey = process.env.SENDGRID_API_KEY;

  if (!apiKey) {
    return {
      valid: false,
    }
  }

  return {
    apiKey,
    channel: 'email',
    fromEmail: process.env.SENDGRID_FROM_EMAIL ?? 'fbgdeacons@gmail.com',
    valid: true
  }
}

export const sendgridConfig: SendgridConfig | EmptyConfig = buildSendgridConfig();
