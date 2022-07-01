export interface Rate {
  sessionId?: string;
  logId?: number;
  imageId?: number;
  imageName?: string;
  similarity?: number;
  attractiveness?: number;
  attractivenessReason?: string;
  safety?: number;
  safetyReason?: string;
  business?: number;
  businessReason?: string;
  keepScores?: boolean;
  imageRateId?: number;
}
